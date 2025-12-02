export interface User {
    id: number;
    username: string;
    role: string;
    balance: number;
    address: Address | null;
}

export interface Address {
    city: string;
    country: string;
    houseNumber: string;
    postalCode: string;
    street: string;
    id: number;
}