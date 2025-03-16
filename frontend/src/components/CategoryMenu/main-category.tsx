import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components"

const MainCategoryWrapper = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-around;
    width: auto;
    height: auto;
`;

const HighLight = styled.div`
    //background-color: #ff4757;
    background-color: white;
    border-radius: 8px;
    position: absolute;
    width: 93.8778px;
    height: 2.5em;
    left: 8.77838px;
    top: 0.75em;
    
    z-index: 1;
    //box-shadow: 0px 0px 12px -2px rgba(255,71,86,0.9);
    box-shadow: 0px 0px 12px -2px white;
    transition: left 0.2s ease,
    width 0.2s ease;    
`
const Selector = styled.ul`
    background-color: #F5F5F5;
    width: auto;
    height: auto;
    //border-radius: 12px;
    position: relative;
    list-style: none;

    display: flex;

    padding: 0.25rem;
    width: min-content;
    position: relative;
`

const SelectorItem = styled.li<{selected?: boolean}>`
    white-space: nowrap;
    padding: 0.75rem 1.5rem;
    cursor: pointer;
    z-index: 2;
    user-select: none;
    transition: color 0.2s ease;
    margin: 0.3rem;
`

interface CategoryComponentProps {
    categories: string[];
    setPage: React.Dispatch<React.SetStateAction<number>>;
}

export default function MainCategory({ categories, setPage }: CategoryComponentProps){
    const [activeIndex, setActiveIndex] = useState<number>(0);
    const targetRef = useRef<(HTMLLIElement | null)[]>([]);
    const highLightRef = useRef<HTMLDivElement | null>(null);
    const parentRef = useRef<(HTMLUListElement | null)>(null);
    const navigate = useNavigate();


    const handleClick = (index: number, category: string) => {
        const target = targetRef.current[index];
        const parent = parentRef.current;
        const highLight = highLightRef.current;
        setActiveIndex(index);

        if(target && parent && highLight) {
            const targetRect = target.getBoundingClientRect();
            const parentRect = parent.getBoundingClientRect();
            highLight.style.width = `${targetRect.width}px`;
            highLight.style.left = `${targetRect.left - parentRect.left}px`;
        }

        setPage(0);
        navigate(`/category/${category.toLowerCase()}`);
    };

    return (
        <MainCategoryWrapper>
            <Selector className="selector" ref={parentRef}>
                <HighLight className="highlight" ref={highLightRef} />
                {
                    categories.map((category, index) => (
                        <SelectorItem
                        key={index}
                        onClick={() => handleClick(index, category)}
                        ref={(el) => (targetRef.current[index] = el)}
                        selected={activeIndex === index} // `selected` prop으로 전달
                    > {category} </SelectorItem>
                    ))
                }
            </Selector>
        </MainCategoryWrapper>
    );
}