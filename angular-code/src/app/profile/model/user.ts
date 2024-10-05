export interface User {
  login: string,
  firstName: string,
  lastName: string,
  position: string,
  oldPassword?: string;
  newPassword?: string;
  dateOfBirth: string,
  email: string,
}
