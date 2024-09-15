import styled from "styled-components"
import { ImAppleinc} from "react-icons/im";
import { FaUser } from "react-icons/fa";
import SearchBar from "./searchBar";

const HeaderContainer = styled.div`
    display: flex; 
    align-items: center;
    justify-content: space-around;
    width: 1190px;
    height: 100px;
    background-color: white;

    .logo{
        font-size: 40px;
    }

    .user {
      font-size: 40px;
    }
`;

export default function Header(){
    return(
      <HeaderContainer>
        <ImAppleinc className="logo"/>

          <SearchBar>
          </SearchBar>

        <FaUser className="user"/>
      </HeaderContainer>  
    );
}