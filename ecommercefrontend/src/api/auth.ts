export async function login(username: string, password: string) {
    return await fetch("http://localhost:8080/api/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username, password}),
        credentials: "include",
    });

}

export async function logout() {
    return fetch("http://localhost:8080/api/logout", {
        method: "POST",
    });


}

export async function register(username: string, password: string) {
    const res = await fetch("http://localhost:8080/api/register", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username, password}),
        credentials: "include",
    });

    if (!res.ok) {
        // registration failed → return the failure response
        return res;
    }

    // If register was successful, automatically log the user in
    return login(username, password);
}

export async function getFullUserInfo() {
    return fetch("http://localhost:8080/api/user/me", {
        credentials: "include"
    });
}

