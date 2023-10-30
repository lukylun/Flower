import React, { useRef, useEffect } from 'react';

export default function OpenViduVideoComponent({ streamManager }) {
    const videoRef = useRef();

    useEffect(() => {
        if (streamManager && videoRef.current) {
            streamManager.addVideoElement(videoRef.current);
        }
    }, [streamManager]);

    return <video className='w-[90%] border-2 border-[#5E0000] rounded-lg cursor-pointer' autoPlay={true} ref={videoRef} />;
}