import type {Item} from "../types/Item.ts";
import type {UpdateItemDTO} from "../types/UpdateItemDTO";
import type {AddItemDTO} from "../types/AddItemDTO";

export const placeOrder = async (itemId: number, quantity: number) => {
    const res = await fetch("http://localhost:8080/api/transaction", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({itemId, quantity}),
        credentials: "include",
    });
    if (!res.ok) {
        const data = await res.json();
        throw new Error(data.detail || "Failed to place order");
    }
    return res;
};
export const getInventory = async () => {
    const res = await fetch("http://localhost:8080/api/inventory", {
        method: "GET",
        credentials: "include",
    });
    const data: Item[] = await res.json();

    if (!res.ok) {
        throw new Error("Failed to fetch items");
    }

    return data;
};
export const getItem = async (itemId: number) => {
    const response = await fetch(`http://localhost:8080/api/inventory/item?itemId=${itemId}`, {
        method: "GET",
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error(`Failed to fetch items: ${response.status}`);
    }

    const data: Item = await response.json();

    return data;
}


export const patchItem = async (updateItem: UpdateItemDTO) => {
    const response = await fetch(`http://localhost:8080/api/inventory`, {
        method: "PATCH",
        body: JSON.stringify(updateItem),
        headers: {"Content-Type": "application/json"},
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error(`Failed to fetch items: ${response.status}`);
    }

    return response;
}
export const addItem = async (updateItem: AddItemDTO) => {
    const response = await fetch(`http://localhost:8080/api/inventory`, {
        method: "POST",
        body: JSON.stringify(updateItem),
        headers: {"Content-Type": "application/json"},
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error(`Failed to add item: ${response.status}`);
    }

    return response;
}
export const deleteItem = async (itemId: number) => {
    const response = await fetch(`http://localhost:8080/api/inventory?itemId=${itemId}`, {
        method: "DELETE",
        headers: {"Content-Type": "application/json"},
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error(`Failed to delete: ${response.status}`);
    }

    return response;
}
