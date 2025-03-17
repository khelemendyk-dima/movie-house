export interface Movie {
    id?: number;
    title: string;
    description: string;
    duration: number;
    ageRating: string;
    releaseDate: string;
    posterUrl: string;
    genres: string[];
}
