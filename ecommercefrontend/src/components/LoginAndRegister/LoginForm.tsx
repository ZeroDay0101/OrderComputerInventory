import {useState} from "react";
import "../../styles/LoginForm.css";

type LoginFormProps = {
    onLogin: (username: string, password: string) => void;
    loading: boolean;
    error: string | null;
};

export default function LoginForm({onLogin, loading, error}: LoginFormProps) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onLogin(username, password);
    };

    return (
        <div className="login-container">
            <form className="login-card" onSubmit={handleSubmit}>
                <h2>Welcome Back</h2>
                {error && <p className="error-message">{error}</p>}

                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <button type="submit" disabled={loading}>
                    {loading ? "Logging in..." : "Log In"}
                </button>

                <p className="signup-text">
                    Don't have an account? <a href="http://localhost:5173/register">Sign up</a>
                </p>
            </form>
        </div>
    );
}
