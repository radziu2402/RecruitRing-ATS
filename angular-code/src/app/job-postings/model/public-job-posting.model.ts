import {Requirement} from "../../dashboard/job-management/model/requirement.model";
import {JobCategory} from "../../dashboard/job-management/model/job-category.model";
import {Location} from "../../dashboard/job-management/model/location.model";
import {Title} from "../../dashboard/job-management/model/title.model";

export interface PublicJobPosting {
  id: number;
  title: Title;
  description: string;
  offerCode: string;
  requirements: Requirement[];
  location: Location;
  workType: string;
  createdAt: [number, number, number];
  jobCategory: JobCategory;
}
