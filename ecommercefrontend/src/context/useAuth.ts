import {useContext} from "react";
import type {AuthContextType} from "./AuthContext";
import AuthContext from "./AuthContext";

//This is a custom React hook to make accessing your AuthContext easier.
export function useAuth(): AuthContextType {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}
