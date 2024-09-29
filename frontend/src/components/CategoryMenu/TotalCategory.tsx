import styled from "styled-components";
import MainCategory from "./main-category";

const CategoryTotalWrapper = styled.div`
    display: flex;
    text-align: center;
    justify-content: center;
    width: 1190px;
    height: 60px;
    background-color: #DCE2F0;

    margin-top: 4rem;
    margin-bottom: 4rem;
`;

export default function Category(){
    
    return(
        <CategoryTotalWrapper>
            <MainCategory categories={['HOME','OUTER', 'PET', 'SHOESdsadasdsads']}/>
        </CategoryTotalWrapper>
    )
}