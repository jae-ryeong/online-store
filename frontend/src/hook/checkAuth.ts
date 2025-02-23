import axios from "axios";

export const checkAuth = async () => {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        return;
    }

    try {
        const response = await axios.post("http://localhost:8080/api/v1/user/auth/check", {},
            {
                headers: {
                    Authorization: `Bearer ${token}`
                },
            });
        if (response.data.valid) {
        } else {
            localStorage.removeItem("accessToken");
            window.location.reload();
        }
    } catch (error) {
        console.error("JWT 검증 실패: ", error);
        localStorage.removeItem("accessToken");
        window.location.reload();
    }
}