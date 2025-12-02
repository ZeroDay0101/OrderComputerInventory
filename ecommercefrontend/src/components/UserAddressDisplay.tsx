import {useEffect, useState} from "react";
import {getUser} from "../api/user";
import type {User} from "../types/User.ts";

interface UserAddressDisplayProps {
    userId?: number;
}

export default function UserAddressDisplay({userId}: UserAddressDisplayProps) {
    const [address, setAddress] = useState<User["address"] | null>(null);

    useEffect(() => {
        if (!userId) return;

        const fetchUser = async () => {
            try {
                const fullUser = await getUser(userId);
                setAddress(fullUser.address);
            } catch (err) {
                setAddress(null);
            }
        };

        fetchUser();
    }, [userId]);

    if (!address) return <p>No address on file</p>;

    return (
        <div className="user-address">
            <p>
                <strong>Address:</strong> {address.country},{address.city},{address.postalCode},{address.city},{address.street},{address.houseNumber}
            </p>
        </div>
    );
}
