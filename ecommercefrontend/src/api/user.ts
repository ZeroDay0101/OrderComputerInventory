import type {UserUpdate} from "../pages/AccountSettingsPage.tsx";
import type {Address, User} from "../types/User.ts";

export const updateUser = async (payload: UserUpdate) => {
    const res = await fetch("http://localhost:8080/api/user", {
        method: "PATCH",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(payload),
        credentials: "include",
    });
    const data = await res.json();

    if (!res.ok) {
        throw new Error(data.detail || "Failed to update user");
    }


    return data;
};

export const updateUserAddress = async (payload: Address) => {
    const res = await fetch("http://localhost:8080/api/user/address", {
        method: "PATCH",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(
            payload,
        ),
        credentials: "include",
    });
    const data = await res.json();

    if (!res.ok) {
        throw new Error(data.detail || "Failed to update user");
    }


    return data;
};

export const getUser = async (userId: number) => {
    const response = await fetch(`http://localhost:8080/api/user?id=${userId}`, {
        method: "GET",
        headers: {"Content-Type": "application/json"},
        credentials: "include",
    });

    if (!response.ok) {
        throw new Error(`Failed to fetch user: ${response.statusText}`);
    }

    const data: User = await response.json();
    return data; // this will be the full User object
};
