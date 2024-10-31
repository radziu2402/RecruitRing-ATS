export function mapApplicationStatusToPolish(status: string): string {
  const statusMapping: { [key: string]: string } = {
    NEW: 'Nowa',
    CV_REVIEW: 'Analiza CV',
    CV_REJECTED: 'Odrzucone CV',
    PHONE_INTERVIEW: 'Rozmowa telefoniczna',
    MEETING: 'Spotkanie',
    REJECTED_AFTER_MEETING: 'Odrzucone po spotkaniu',
    SECOND_STAGE: 'Drugi etap',
    OFFER_MADE: 'Oferta złożona',
    HIRED: 'Zatrudniony',
    APPLICATION_WITHDRAWN: 'Aplikacja wycofana'
  };
  return statusMapping[status] || status;
}
