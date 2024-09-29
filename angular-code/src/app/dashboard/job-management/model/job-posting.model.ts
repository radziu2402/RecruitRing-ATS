import {Title} from "./title.model";
import {Requirement} from "./requirement.model";
import {Recruiter} from "./recruiter.model";
import {Location} from "./location.model";
import {JobCategory} from "./job-category.model";

export interface JobPosting {
  id: number;
  title: Title;
  description: string;
  offerCode: string;
  location: Location;
  workType: string;
  createdAt: [number, number, number];
  recruiters: Recruiter[];
  requirements: Requirement[];
  jobCategory: JobCategory;
}
