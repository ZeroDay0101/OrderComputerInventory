export interface Order {
    transactionId: number;
    userId: number;
    itemId: number;
    quantity: number;
    status: string;
}