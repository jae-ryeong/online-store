import axios from "axios";
import { createContext, ReactNode, useContext, useEffect, useState } from "react";

interface AuthContextType {
    isLogin: boolean;
    auth: string | null;
    login: (token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({
    isLogin: false,
    auth: null,
    login: (token: string) => { },
    logout: () => {},
});

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [auth, setAuth] = useState<string | null>(null);

    const login = (token: string) => {
        setAuth(token);
    }

    const logout = () => {
        setAuth(null);
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.reload();   // 로그아웃시 페이지 새로고침
    }

    useEffect(() => {
        const checkAuth = async () => {
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
                    setAuth(token);
                } else {
                    localStorage.removeItem("accessToken");
                    setAuth(null);
                    window.location.reload();
                }
            } catch (error) {
                console.error("JWT 검증 실패: ", error);
                localStorage.removeItem("accessToken");
                //localStorage.removeItem("refreshToken");
                setAuth(null);
                window.location.reload();
            }
        }
        checkAuth();
    }, []);

    const isLogin = !!auth;

    return (
        <AuthContext.Provider value={{ isLogin, auth, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext)
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};