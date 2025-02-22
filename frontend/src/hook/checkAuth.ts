import axios from "axios";

export const checkAuth = async () => {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        return;
    }

    try {
        console.log(token);
        const response = await axios.post("http://localhost:8080/api/v1/user/auth/check", {},
            {
                headers: {
                    Authorization: `Bearer ${token}`
                },
            });
        console.log(response.data);
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