import {useRef, useState} from "react";
import "/src/styles/UserMenu.css";
import {useAuth} from "../context/useAuth.ts";
import {useNavigate} from "react-router-dom";
import type {User} from "../types/User";

interface UserMenuProps {
    user: User;   // user is required, never null
}

export default function UserMenu({user}: UserMenuProps) {
    const [open, setOpen] = useState(false);
    const timeoutRef = useRef<number | null>(null);
    const {setUser} = useAuth();
    const navigate = useNavigate();

    const showMenu = () => {
        if (timeoutRef.current) clearTimeout(timeoutRef.current);
        setOpen(true);
    };
    const hideMenu = () => {
        timeoutRef.current = setTimeout(() => {
            setOpen(false);
        }, 50); // 👈 wait 250ms before closing (adjust if you want)
    };

    const logoutUser = async () => {
        try {
            await fetch("http://localhost:8080/api/logout", {
                method: "POST",
                credentials: "include",
            });
            setUser(null);
        } catch (err) {
            console.error("Logout failed:", err);
        }
    };
    return (
        <div
            className="user-menu"
            onMouseEnter={() => showMenu()}
            onMouseLeave={() => hideMenu()}
        >
            <span className="username">
                Logged in as: {user.username}
            </span>


            {open && (
                <ul className="dropdown">
                    <li onClick={() => (navigate("/user"))}>
                        Account Settings
                    </li>
                    <li onClick={() => navigate("/history")}>
                        Transaction History
                    </li>
                    {user.role == "ADMIN" && (
                        <div className={"admin-buttons"}>
                            <li onClick={() => navigate("/additems")}>
                                Add/Patch items
                            </li>
                        </div>

                    )}
                    <li onClick={() => {
                        logoutUser()
                    }}>
                        Log-out
                    </li>
                </ul>
            )}

        </div>
    );
}
