import Pagination from "react-js-pagination";
import styled from "styled-components";


interface props{
    page:number;
    setPage: React.Dispatch<React.SetStateAction<number>>;
    totalElements: number;
    totalPages: number;
}

const PaginationBox = styled.div`
  .pagination { display: flex; justify-content: center; margin-top: 15px;}
  ul { list-style: none; padding: 0; }
  ul.pagination li {
    display: inline-block;
    width: 30px;
    height: 30px;
    border: 1px solid #e2e2e2;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1rem; 
  }
  ul.pagination li:first-child{ border-radius: 5px 0 0 5px; }
  ul.pagination li:last-child{ border-radius: 0 5px 5px 0; }
  ul.pagination li a { text-decoration: none; color: #337ab7; font-size: 1rem; }
  ul.pagination li.active a { color: white; }
  ul.pagination li.active { background-color: #337ab7; }
  ul.pagination li a:hover,
  ul.pagination li a.active { color: blue; }
`

export default function PaginationComponent({page,setPage,totalElements,totalPages}:props){

    const handlePageChange = (page:number) => {
        setPage(page-1)
    }

    return(        
        <PaginationBox>
            <Pagination
            activePage={page+1}
            itemsCountPerPage={20}
            totalItemsCount={totalElements}
            pageRangeDisplayed={totalPages}
            onChange={handlePageChange}
        />
        </PaginationBox>        
    )
}