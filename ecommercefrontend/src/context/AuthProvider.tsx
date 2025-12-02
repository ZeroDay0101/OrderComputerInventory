import {type ReactNode, useEffect, useState} from "react";
import type {User} from "./AuthContext"
import AuthContext from "./AuthContext";

interface AuthProviderProps {
    children: ReactNode;
}

export function AuthProvider({children}: AuthProviderProps) {
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        if (!user) {
            async function fetchUser() {
                try {
                    const res = await fetch("http://localhost:8080/api/user/me", {
                        credentials: "include"
                    });
                    if (res.ok) {
                        const data: User = await res.json();
                        setUser(data);
                    }
                } catch (err) {
                    console.error("Failed to fetch user:", err);
                }
            }

            fetchUser();
        }
    }, [user]);
    //The provider passes { user, setUser } down to any components that consume the context
    return (
        <AuthContext.Provider value={{user, setUser}}>
            {children}
        </AuthContext.Provider>
    );
}
