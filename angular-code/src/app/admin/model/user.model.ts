export interface UserDTO {
  id?: number;
  login: string;
  email: string;
  position?: string;
  firstName?: string;
  lastName?: string;
  dateOfBirth?: string;
  locked: boolean;
}
