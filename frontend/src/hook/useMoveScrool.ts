import { useRef } from 'react';

const useMoveScroll = () => {
    const elementRef = useRef<HTMLDivElement | null>(null);

    const onMoveToElement = () => {
        if (elementRef.current) {
            elementRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    };

    return { elementRef, onMoveToElement };
};

export default useMoveScroll;