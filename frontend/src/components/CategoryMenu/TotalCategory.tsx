import styled from "styled-components";
import MainCategory from "./main-category";

const CategoryTotalWrapper = styled.div`
    display: flex;
    text-align: center;
    justify-content: center;
    width: 1190px;
    height: 60px;
    background-color: #F5F5F5;

    margin-top: 4rem;
    margin-bottom: 4rem;
`;

interface CategoryComponentProps {
    setPage: React.Dispatch<React.SetStateAction<number>>
}

export default function Category({setPage} : CategoryComponentProps){
    
    return(
        <CategoryTotalWrapper>
            <MainCategory categories={['HOME','BOOK', 'FOOD', 'CLOTHES', 'PET']} setPage={setPage}/>
        </CategoryTotalWrapper>
    )
}