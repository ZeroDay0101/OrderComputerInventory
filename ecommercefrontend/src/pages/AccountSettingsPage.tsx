import React, {useEffect, useState} from "react";
import {useAuth} from "../context/useAuth";
import {updateUser, updateUserAddress} from "../api/user";
import type {Address, User} from "../types/User";
import "../styles/AccountSettingsPage.css";

export type UserUpdate = Partial<Omit<User, "id">> & { id: number };

export default function AccountSettingsPage() {
    const {user, setUser} = useAuth();
    const [originalData, setOriginalData] = useState<User | null>(null);
    const [error, setError] = useState<Error | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const defaultAddress: Address = {
        id: 0,
        city: "",
        country: "",
        houseNumber: "",
        postalCode: "",
        street: "",
    };
    useEffect(() => {
        if (!user?.id) return;

        const fetchUser = async () => {
            try {
                if (!user) return;
                setOriginalData(user);
            } catch (err) {
                setError(err as Error);
            }
        };

        fetchUser();
    }, [user?.id]);
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!user?.id || !originalData) return;

        const payload: UserUpdate = {id: user.id};

        if (user.username !== originalData.username) payload.username = user.username;
        if (user.role !== originalData.role) payload.role = user.role;
        if (user.balance !== originalData.balance) payload.balance = user.balance;

        try {
            const updatedUser: User = await updateUser(payload);

            if (user.address) {
                user.address.id = user.id;
                const address: Address = {...defaultAddress, ...user.address};
                await updateUserAddress(address);
                updatedUser.address = address;
            }
            setUser(updatedUser);
            setOriginalData(updatedUser);
            setError(null);
            setSuccess("Changes successful")
        } catch (err) {
            setError(err as Error);
        }
    };


    const handleAddressChange = (field: keyof Address, value: string) => {
        setUser(prev => ({
            ...prev!,
            address: {
                ...(prev?.address || defaultAddress),
                [field]: value,
            },
        }));
    };

    return (
        <div className={"accountPageContainer"}>
            <h1>User dashboard</h1>
            <form className={"usernameForm"} onSubmit={handleSubmit}>
                <label>Username:</label>
                <input
                    type="text"
                    value={user?.username || ""}
                    onChange={(e) => setUser(prev => ({...prev!, username: e.target.value}))}
                    required
                /><br/>

                <label>Role:</label>
                <input
                    type="text"
                    value={user?.role || ""}
                    onChange={(e) => setUser(prev => ({...prev!, role: e.target.value}))}
                    disabled={originalData?.role !== "ADMIN"}
                    required
                /><br/>

                <label>Balance:</label>
                <input
                    type="number"
                    value={user?.balance ?? 0}
                    onChange={(e) => setUser(prev => ({...prev!, balance: Number(e.target.value)}))}
                    disabled={originalData?.role !== "ADMIN"}
                    required
                /><br/>

                <h2>Edit Address Info</h2>

                {(["country", "city", "postalCode", "street", "houseNumber"] as (keyof Address)[]).map(field => (
                    <div key={field}>
                        <label>{field.charAt(0).toUpperCase() + field.slice(1)}:</label>
                        <input
                            type="text"
                            value={user?.address?.[field] || ""}
                            onChange={(e) => handleAddressChange(field, e.target.value)}
                            required
                        /><br/>
                    </div>
                ))}

                <button type="submit">Submit</button>

                {error && <strong id="errorMessage" style={{color: "red"}}>{error.message}</strong>}
                {success && <strong id="successMessage" style={{color: "green"}}>{success}</strong>}
            </form>
        </div>
    );
}
