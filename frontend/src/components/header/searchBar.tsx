import styled from "styled-components"
import { MdOutlineCancel } from "react-icons/md";
import { PiMagnifyingGlass } from "react-icons/pi";
import { useState } from "react";

const SearchContainer = styled.form`
  width: 35%;
  height: 50%;

  display: flex;
  align-items: center;
  justify-content: space-between;

  border-bottom: 1px solid gray;

  .deleteButton{
    font-size: 20px;
  }
  .searchButton{
    font-size: 25px;
  }
`;

const SearchDeleteButton = styled.button<{$deleteButtonShow:boolean}>`
  background-color: transparent;
  border: none;
  cursor: pointer;
  
  display: ${({$deleteButtonShow}) => $deleteButtonShow ? "block" : "none"};
  &:hover{
    color: #db0f0fd3;
  }
`;

const SearchClick = styled.button`
  background-color: transparent;
  border: none;
  cursor: pointer;
`;

const SearchBarContainer = styled.input`
    width: 80%;
    height: 80%;
    font-size: 1.2rem;
    border:none;
    outline: none;
    background-color: transparent;   
    font-family : GmarketMedium;
`;

export default function SearchBar(props:any) { // Todo: any 타입 고치기
    const [searchKeyword, setSearchKeyword] = useState<string>("");

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setSearchKeyword(e.target.value);
    }

    const clearSearch = () => {
      setSearchKeyword(""); // 검색어 초기화
    };

    return(
        <SearchContainer>
            <SearchBarContainer placeholder="검색어를 입력해주세요." 
            type="text"
            value={searchKeyword}
            onChange={handleInputChange}
            /> 

            <SearchDeleteButton 
            $deleteButtonShow={searchKeyword != ""} 
            onClick={clearSearch} type="button">
              <MdOutlineCancel className="deleteButton"/>
            </SearchDeleteButton>

            <SearchClick>
              <PiMagnifyingGlass className="searchButton" />
            </SearchClick>
        </SearchContainer>
        
    );
}