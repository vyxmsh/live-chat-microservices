import { useEffect,useRef,useState } from "react";
import api from '../lib/api';

//Polls presence status for a list of userIdsevery 30s

export function usePresence(userIds: number[]){
    const[presenceMap, setPresenceMap]= useState<Record<number,boolean>>({});

    useEffect(() =>{
        if(userIds.length===0) return;

        const fetchPresence = async () => {
            const results = await Promise.all(
                userIds.map((id) => 
                api
                .get('/api/presence/${id}')
                .then((r)=>({id, online: r.data.online as boolean}))
                .catch(() => ({id, online: false}))
            )
            );

            const map = results.reduce<Record<number, boolean>>((acc, {id, online}) => {
                acc[id] = online;
                return acc;
            },{});

            setPresenceMap(map);
        };

        fetchPresence();
        const interval = setInterval(fetchPresence,3000);
        return () => clearInterval(interval);
    },[userIds.join(',')]);

    return presenceMap;
}

// Sends heartbeat every 30s so the current user stays online
export function useHeartbeat() {
    useEffect(() => {
        const sendHeartbeat = () => {
            api.post('/api/presence/heartbeat').catch(() => {});
        };

        sendHeartbeat();
        const interval = setInterval(sendHeartbeat,30000);
        return () => clearInterval(interval);
    },[]);
}