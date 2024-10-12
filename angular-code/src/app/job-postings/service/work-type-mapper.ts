export const workTypeMap: { [key: string]: string } = {
  STATIONARY: 'Stacjonarna',
  REMOTE: 'Zdalna',
  HYBRID: 'Hybrydowa'
};

export function mapWorkType(workType: string): string {
  return workTypeMap[workType] || workType;
}
