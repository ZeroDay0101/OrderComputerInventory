import React from "react";
import {Navigate} from "react-router-dom";
import {useAuth} from "../context/useAuth.ts";

export function ProtectedRoute({children}) {
    const {user} = useAuth();

    // If not logged in, redirect to /login
    if (!user) {
        return <Navigate to="/login" replace/>;
    }

    // If logged in, show the child component (the protected page)
    return children;
}