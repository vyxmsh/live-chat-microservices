'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import api from '../lib/api';
import { Channel } from '../lib/types';

export default function Sidebar() {
    const [channels, setChannels] = useState<Channel[]>([]);
    const [newChannelName, setNewChannelName] = useState('');
    const [creating, setCreating] = useState(false);
    const pathName = usePathname();
    const router = useRouter();

    const username = typeof window !== 'undefined' ? localStorage.getItem('username') ?? '' : '';

    useEffect(() => {
        api.get<Channel[]>('/api/channels')
           .then((r) => setChannels(r.data))
           .catch(console.error);
    }, []);

    const createChannel = async() => {
        if(!newChannelName.trim()) return;
        try{
            const res = await api.post<Channel>('/api/channels',{
                name: newChannelName.trim(),
            });
        setChannels((prev) => [...prev,res.data]);
        setNewChannelName('');
        setCreating(false);
        router.push('/app/channel/${res.data.id}');
        }catch (err){
            console.error('Failed to create channel:', err);
        }
        };

        const logout = () => {
            api.delete(`/api/presence/${localStorage.getItem('userId')}`).catch(()=>{});
            localStorage.clear();
            router.push('/login');
        };

        return (
    <div className="w-64 bg-gray-800 flex flex-col h-full">
      {/* App name */}
      <div className="px-4 py-4 border-b border-gray-700">
        <h1 className="text-white font-bold text-lg">SlackClone</h1>
        <p className="text-gray-400 text-xs mt-0.5">{username}</p>
      </div>

      {/* Channels list */}
      <div className="flex-1 overflow-y-auto py-4">
        <div className="px-4 mb-2 flex items-center justify-between">
          <span className="text-gray-400 text-xs font-semibold uppercase tracking-wider">
            Channels
          </span>
          <button
            onClick={() => setCreating(true)}
            className="text-gray-400 hover:text-white text-lg leading-none"
            title="New channel"
          >
            +
          </button>
        </div>

        {/* New channel input */}
        {creating && (
          <div className="px-4 mb-2 flex gap-1">
            <input
              autoFocus
              className="flex-1 bg-gray-700 text-white text-sm rounded px-2 py-1
                         focus:outline-none focus:ring-1 focus:ring-indigo-500"
              placeholder="channel-name"
              value={newChannelName}
              onChange={(e) => setNewChannelName(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') createChannel();
                if (e.key === 'Escape') setCreating(false);
              }}
            />
            <button
              onClick={createChannel}
              className="text-indigo-400 hover:text-indigo-300 text-sm"
            >
              Add
            </button>
          </div>
        )}

        {channels.map((channel) => {
          const isActive = pathName === `/app/channel/${channel.id}`;
          return (
            <Link
              key={channel.id}
              href={`/app/channel/${channel.id}`}
              className={`flex items-center gap-2 px-4 py-1.5 text-sm transition-colors ${
                isActive
                  ? 'bg-indigo-700 text-white'
                  : 'text-gray-400 hover:bg-gray-700 hover:text-white'
              }`}
            >
              <span className="text-gray-500">#</span>
              {channel.name}
            </Link>
          );
        })}
      </div>

      {/* Logout */}
      <div className="px-4 py-3 border-t border-gray-700">
        <button
          onClick={logout}
          className="text-gray-400 hover:text-white text-sm transition-colors"
        >
          Sign out
        </button>
      </div>
    </div>
  );

    }