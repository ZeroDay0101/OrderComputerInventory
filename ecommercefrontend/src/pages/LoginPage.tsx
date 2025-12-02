import {useState} from "react";
import {getFullUserInfo, login} from "../api/auth";
import LoginForm from "../components/LoginAndRegister/LoginForm.tsx";
import {useAuth} from "../context/useAuth";
import {useNavigate, useSearchParams} from "react-router-dom";

interface User {
    username: string;
}

export default function LoginPage() {
    const [loading, setLoading] = useState<boolean>(false);
    const [searchParams] = useSearchParams();
    const [error, setError] = useState<string | null>(
        searchParams.get("error")
    );
    const {setUser} = useAuth();
    const navigate = useNavigate();

    const handleLogin = async (username: string, password: string) => {
        setLoading(true);
        setError(null);

        try {
            const loginRes = await login(username, password);
            const res: User | any = await getFullUserInfo();

            if (!loginRes.ok || !res.ok) {
                throw new Error(res.error || "Login failed");
            }
            const data: User | any = await res.json().catch(() => ({}));


            setUser(data);
            navigate("/inventory");
        } catch (err: any) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <LoginForm
            onLogin={handleLogin}
            loading={loading}
            error={error}
        />
    );
}
