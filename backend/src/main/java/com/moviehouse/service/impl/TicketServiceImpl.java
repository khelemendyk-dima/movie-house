package com.moviehouse.service.impl;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.BorderRadius;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.moviehouse.dto.TicketDto;
import com.moviehouse.exception.*;
import com.moviehouse.model.Ticket;
import com.moviehouse.repository.TicketRepository;
import com.moviehouse.service.BookingService;
import com.moviehouse.service.TicketService;
import com.moviehouse.util.ConvertorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${tickets.pdf.dir}")
    private String ticketsDir;

    @Value("${images.dir}")
    private String imgDir;

    private final TicketRepository ticketRepository;
    private final BookingService bookingService;
    private final ConvertorUtil convertor;

    @Override
    public List<TicketDto> getPaidTicketsBySessionId(Long sessionId) {
        log.info("Fetching paid tickets for sessionId: {}", sessionId);

        List<TicketDto> tickets = ticketRepository.findPaidTicketsBySessionId(sessionId).stream()
                .map(convertor::toTicketDto)
                .toList();

        log.info("Found {} paid tickets for sessionId: {}", tickets.size(), sessionId);

        return tickets;
    }

    @Override
    public byte[] generateTicketsPdf(List<Ticket> tickets) {
        log.info("Generating Ticket PDF for {} tickets", tickets.size());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Logo
            String logoPath = imgDir + "logo.jpg";
            Image logo = new Image(ImageDataFactory.create(logoPath))
                    .setWidth(150)
                    .setHeight(150)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(logo);

            for (int i = 0; i < tickets.size(); i++) {
                Ticket ticket = tickets.get(i);
                String validationUrl = baseUrl + "/api/tickets/validate/" + ticket.getId();

                // Alternating colors for better visibility
                Color bgColor = (i % 2 == 0) ? new DeviceRgb(230, 230, 250) : new DeviceRgb(255, 248, 220);
                Table table = new Table(new float[]{4, 2}).useAllAvailableWidth();
                table.setBackgroundColor(bgColor);
                table.setPadding(10);
                table.setBorderRadius(new BorderRadius(10));

                // Left side: Movie info
                Cell detailsCell = new Cell()
                        .add(new Paragraph(ticket.getSession().getMovie().getTitle())
                                .setFontSize(20)
                                .setBold()
                                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                                .setTextAlignment(TextAlignment.LEFT)
                                .setPaddingBottom(5))
                        .add(new Paragraph("Hall: " + ticket.getSession().getHall().getName())
                                .setFontSize(14)
                                .setFont(PdfFontFactory.createFont(StandardFonts.COURIER))
                                .setPaddingBottom(3))
                        .setPaddingBottom(15)
                        .add(new Paragraph("Seat: Row " + ticket.getSeat().getRowNumber() + " Seat " + ticket.getSeat().getSeatNumber())
                                .setFontSize(14)
                                .setFont(PdfFontFactory.createFont(StandardFonts.COURIER))
                                .setPaddingBottom(3))
                        .setPaddingBottom(15)
                        .add(new Paragraph("Date: " + ticket.getSession().getStartTime().toLocalDate() + " " + ticket.getSession().getStartTime().toLocalTime())
                                .setFontSize(14)
                                .setFont(PdfFontFactory.createFont(StandardFonts.COURIER))
                                .setPaddingBottom(3))
                        .setPadding(15)
                        .setBorder(Border.NO_BORDER);
                table.addCell(detailsCell);

                // Right side: QR code
                BarcodeQRCode qrCode = new BarcodeQRCode(validationUrl);
                Image qrImage = new Image(qrCode.createFormXObject(pdf))
                        .setWidth(120)
                        .setHeight(120)
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT);

                Cell qrCell = new Cell().add(qrImage)
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBorder(Border.NO_BORDER)
                        .setPadding(10);
                table.addCell(qrCell);

                document.add(table);
                document.add(new Paragraph("\n"));
            }

            document.close();

            log.info("PDF generation completed");

            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            throw new ServiceException("Error generating PDF: " + e.getMessage());
        }
    }

    @Override
    public void saveTicketsPdf(byte[] pdf, Long bookingId) {
        log.info("Saving Ticket PDF for bookingId: {}", bookingId);

        try {
            String fileName = "ticket-" + bookingId + ".pdf";
            Path path = Paths.get(ticketsDir).resolve(fileName);

            Files.createDirectories(path.getParent());
            Files.write(path, pdf);

            log.info("Ticket PDF for bookingId={} saved successfully at path: {}", bookingId, path);
        } catch (IOException e) {
            throw new TicketSaveException(bookingId, e.getMessage());
        }
    }

    @Override
    public byte[] getTicketsPdf(Long bookingId) {
        log.info("Retrieving ticket PDF for bookingId: {}", bookingId);

        checkBookingIsPaid(bookingId);

        String fileName = "ticket-" + bookingId + ".pdf";
        Path path = Paths.get(ticketsDir).resolve(fileName);

        if (!Files.exists(path)) {
            throw new TicketFileNotFoundException(bookingId);
        }

        try {
            byte[] pdfBytes = Files.readAllBytes(path);
            log.info("Successfully retrieved PDF for bookingId: {}", bookingId);

            return pdfBytes;
        } catch (IOException e) {
            throw new TicketReadException(bookingId, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void validateTicket(Long ticketId) {
        log.info("Validating ticket with id={}", ticketId);

        Ticket ticket = findTicketById(ticketId);

        checkBookingIsPaid(ticket.getBooking().getId());
        checkTicketIsNotUsed(ticket);

        ticket.setUsed(true);
        ticketRepository.save(ticket);

        log.info("Ticket with id={} marked as used", ticketId);
    }

    private Ticket findTicketById(Long ticketId) {
        log.info("Searching ticket with id={}", ticketId);

        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    private void checkBookingIsPaid(Long bookingId) {
        log.info("Checking if booking with id={} is paid", bookingId);

        if (!bookingService.isBookingPaid(bookingId)) {
            throw new BookingNotPaidException(bookingId);
        }
    }

    private void checkTicketIsNotUsed(Ticket ticket) {
        if (ticket.isUsed()) {
            throw new TicketAlreadyUsedException(ticket.getId());
        }
    }
}