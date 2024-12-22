import styled from "styled-components"
import { ImAppleinc} from "react-icons/im";
import SearchBar from "./searchBar";
import { Link } from "react-router-dom";
import { useAuth } from "../../AuthProvider";
import "../../fonts/font.css";

const HeaderContainer = styled.div`
    display: flex; 
    align-items: center;
    justify-content: space-around;
    width: 1190px;
    height: 100px;
    background-color: #FBFBFB;

    .logo{
        font-size: 40px;
        :hover{
        cursor: pointer;
        }
        // 클릭시 메인페이지로 이동
    }

    .user {
      font-size: 40px;
      :hover{
        cursor: pointer;
      }
      // 클릭시 마이페이지, 장바구니 등 표시
    }
    border-bottom: rgba(0,0,0,0.3) 1px solid;
`;

const UserBar =  styled.div`
  width: 150px; height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  
  .loginTo{
    padding-right: 10px;
    font-size: 15px;
    text-decoration: none;
    color: inherit;
    font-family: "LineRg";
  }

  .joinTo{
    font-size: 15px;
    text-decoration: none;
    color: inherit;
    font-family: "LineRg";
  }
`;

const LogoutButton = styled.button`
  background-color: transparent;
  font-size: 15px;
  border: none;
  text-decoration: none;
  color: inherit;
  font-family: "LineRg";
  cursor: pointer;
`;
export default function Header(){
  const {isLogin, logout} = useAuth();

    return(
      <HeaderContainer>
        <ImAppleinc className="logo"/>

          <SearchBar>
          </SearchBar>
        
        <UserBar>
          <Link className="loginTo" to={"/login"}>
            {isLogin ? "마이페이지" : `로그인`}
          </Link>
          
          {isLogin ? <LogoutButton onClick={() => logout()}>로그아웃</LogoutButton> : <Link className="joinTo" to={"/join"}>            
            회원가입
          </Link>
          }
        </UserBar>
        

      </HeaderContainer>  
    );
}