import styled from "styled-components"
import "../css/loginPage.css"
import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../../AuthProvider";

const LoginWrapper = styled.div`
    display: block;
    max-width: 680px; width: 80%;
    margin: 120px auto;
`;

const Title = styled.h1`
    color: #e91e63; /* gray도 이쁜듯 */
    font-size: 48px;
    letter-spacing: center;
    margin: 120px 0 80px 0;
    transition: .2s linear;
`;

const LoginForm = styled.form`
    width: 100%; max-width: 680px;
    margin: 40px auto 10px;
`;

export default function Login(){
    const navigate = useNavigate();
    const [userName, setUserName] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [error, setError] = useState<string>("");
    const {login} = useAuth();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try{
            const response = await axios.post("http://localhost:8080/api/v1/user/login",
                {userName, password}
            );

            const accessToken:string = response.data.accessToken;
            const refreshToken:string = response.data.refreshToken;
            localStorage.setItem("accessToken", accessToken); // 토큰을 로컬 스토리지에 저장
            localStorage.setItem("refreshToken", refreshToken);

            // 헤더에 담기
            if(response.status === 200) {
                axios.defaults.headers.common["Authorization"] = `Bearer ${accessToken}`;
            }

            login(accessToken); // 새로고침 하지 않아도 로그인 상태

            navigate('/category/home');
        } catch (err) {
            setError("Invalid username or password");
        }
    }
    
    const homeNav = () => { // 매개변수가 없으므로 타입선언X
        navigate("/category/home")
    }

    return(
        <LoginWrapper className="container">
            <Title onClick={homeNav} style={{cursor:"pointer"}}>Login</Title>
        <LoginForm className="form" action="post" onSubmit={handleLogin}>
            {/* email input */}
            <div className="input_block">
                <input type="text" placeholder="Username" className="input" id="username"
                value={userName}
                onChange={(e) => setUserName(e.target.value)} />            
            </div>

            {/* password input */}
            <div className="input_block">
                <input type="password" placeholder="password" className="input" id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}/>
            </div>

            {/* sign in input */}
            <button className="signin_btn">
                Login
            </button>
        </LoginForm>
        {error && <p style={{color:"red"}}>{error}</p>}

        {/* separator */}
        <div className="separator">
            <p>OR</p>
        </div>

        {/* google button */}
        <button className="google_btn">
            <i className="fa fa-google"></i>
            Sign in with Google
        </button>

        {/* github button */}
        <button className="github_btn">
            <i className="fa fa-github"></i>
            Sign in with Github
        </button>
        </LoginWrapper>
    )
}