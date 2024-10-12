export interface Address {
  city: string;
  postCode: string;
  street?: string;
  streetNumber?: number;
  flatNumber?: number;
}

export interface ApplicationDTO {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  offerCode: string;
  address: Address;
}
