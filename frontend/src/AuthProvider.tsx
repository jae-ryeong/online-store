import { createContext, ReactNode, useContext, useEffect, useState } from "react";

interface AuthContextType {
    isLogin: boolean;
    auth: string|null;
    login: (token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({
    isLogin: false,
    auth: null,
    login: (token: string) => {},
    logout: () => {}
});

export const AuthProvider = ({children} : {children:ReactNode}) => {
    const [auth, setAuth] = useState<string | null>(null);

    useEffect(() => {
        //LocalStorage에서 토큰을 가져와 상태 설정
        const token = localStorage.getItem("accessToken");
        if(token){
            setAuth(token);
        }
    }, []);

    const login = (token:string) => {
        setAuth(token);
    }

    const logout = () => {
        setAuth(null);
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.reload();   // 로그아웃시 페이지 새로고침
    }

    const isLogin = !!auth;

    return(
        <AuthContext.Provider value={{isLogin, auth, login,logout}}>
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