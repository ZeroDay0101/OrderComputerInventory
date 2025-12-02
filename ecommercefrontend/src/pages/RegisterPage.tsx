import {useState} from "react";
import {register} from "../api/auth";
import RegisterForm from "../components/LoginAndRegister/RegisterForm.tsx";
import "../styles/RegisterForm.css";

export default function RegisterPage() {
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const handleRegister = async (username: string, password: string) => {
        setLoading(true);
        setError(null);

        try {
            const res = await register(username, password);
            if (!res.ok) {
                const errorData: any = await res.json().catch(() => ({}));
                throw new Error(errorData.detail || "Registration failed");
            }

            window.location.href = "http://localhost:5173/inventory";
        } catch (err: any) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <RegisterForm
            onRegister={handleRegister}
            loading={loading}
            error={error}
        />
    );
}
