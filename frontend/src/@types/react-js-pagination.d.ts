declare module "react-js-pagination" {
    import React from "react";

    interface PaginationProps {
        activePage: number;
        itemsCountPerPage: number;
        totalItemsCount: number;
        pageRangeDisplayed: number;
        onChange: (pageNumber: number) => void;
        prevPageText?: string;
        nextPageText?: string;
        firstPageText?: string;
        lastPageText?: string;
        innerClass?: string;
        itemClass?: string;
        activeClass?: string;
    }

    const Pagination: React.FC<PaginationProps>;
    export default Pagination;
}