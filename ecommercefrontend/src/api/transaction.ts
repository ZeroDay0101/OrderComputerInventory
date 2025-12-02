import type {Order} from "../types/Order.ts";

export const getUserOrders = async (userId: number) => {
    const response = await fetch(`http://localhost:8080/api/order/user?userId=${userId}`, {
        method: "GET",
        headers: {"Content-Type": "application/json"},
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error(`Failed to fetch transactions: ${response.status}`);
    }

    const data: Order[] = await response.json();
    return data;
};
