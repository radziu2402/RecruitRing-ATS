export interface JobPostingSummary {
  title: string;
  offerCode: string;
  location: string;
  workType: string;
  createdAt: [number, number, number];
  recruiters: string[];
  jobCategory: string;
}
