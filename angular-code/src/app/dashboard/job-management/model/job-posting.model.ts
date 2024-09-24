export interface JobPosting {
  id: number;
  title: string;
  description: string;
  location: string;
  workType: string;
  createdAt: [number, number, number];
  recruiters: string[];
  requirements: string[];
  jobCategory: string;
}
