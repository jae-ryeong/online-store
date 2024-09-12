import styled from "styled-components"
import { ImAppleinc } from "react-icons/im";

const HeaderContainer = styled.div`
    display: flex; 
    align-items: center;
    justify-content: space-around;
    width: 100%;
    height: 100px;
    background-color: white;

    .logo{
        font-size: 40px;
    }
`

export default function Header(){
    return(
      <HeaderContainer>
        <ImAppleinc className="logo"/>
      </HeaderContainer>  
    );
}