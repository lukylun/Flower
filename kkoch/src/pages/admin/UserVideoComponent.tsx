import React from 'react';
import OpenViduVideoComponent from './OvVideo';

export default function UserVideoComponent({ streamManager }) {

    const getNicknameTag = () => {
        // Gets the nickName of the user
        return JSON.parse(streamManager.stream.connection.data).clientData;
    }

    return (
        <>
            {streamManager !== undefined && streamManager.stream.connection.data.indexOf('관리자') === -1 ? (
                <div className="mt-3">
                    <OpenViduVideoComponent streamManager={streamManager} />
                    <div><p className='font-bold'>{getNicknameTag()}</p></div>
                </div>
                    
            ) : null}
        </>
    );
}