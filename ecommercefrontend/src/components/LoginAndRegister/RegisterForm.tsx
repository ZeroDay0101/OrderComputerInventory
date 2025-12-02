import {useState} from "react";

type RegisterFormProps = {
    onRegister: (username: string, password: string) => void;
    loading: boolean;
    error: string | null;
};

export default function RegisterForm({onRegister, loading, error}: RegisterFormProps) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onRegister(username, password);
    };

    return (
        <div className="register-container">
            <form className="register-card" onSubmit={handleSubmit}>
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
                    {loading ? "Register in progress..." : "Register"}
                </button>
            </form>
        </div>
    );
}
