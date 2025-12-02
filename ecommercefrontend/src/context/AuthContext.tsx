import React, {createContext} from "react";
import type {User} from "../types/User.ts";

// Define the user type according to your backend response


// Define the shape of your context
interface AuthContextType {
    user: User | null;
    setUser: React.Dispatch<React.SetStateAction<User | null>>;
}

// Use default value as null (will be provided by the provider)
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export default AuthContext;
export type {User, AuthContextType};
