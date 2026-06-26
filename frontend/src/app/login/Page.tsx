'use effect';

import { useState, FormEvent } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import api from '../../lib/api';
import { Authresponse } from '../../lib/types';

export default function LoginPage() {
    const[username, setUsername] = useState('');
    const[password, setPassword] = useState('');
    const[error, setError] = useState('');
    const[loading, setLoading] = useState(false);
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try{
            const res = await api.post<Authresponse>('/api/auth/login',{
                username,
                password,
            });

            localStorage.setItem('token', res.data.token);
            localStorage.setItem('token', res.data.username);

            //Decode userId from JWT payload(base 64 middle section)
            const payload  = JSON.parse(atob(res.data.token.split('.')[1]));
            localStorage.setItem('userID', payload.userId?.toString()?? '');

            router.push('/app');
        } catch {
            setError('Invalid username or password');
        } finally {
            setLoading(false);
        }
    };
return (
    <div className="min-h-screen bg-gray-900 flex items-center justify-center">
      <div className="bg-gray-800 rounded-xl p-8 w-full max-w-sm">
        <h1 className="text-white text-2xl font-bold mb-6">Sign in</h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-gray-400 text-sm mb-1">Username</label>
            <input
              type="text"
              required
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full bg-gray-700 text-white rounded-lg px-4 py-2
                         focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          <div>
            <label className="block text-gray-400 text-sm mb-1">Password</label>
            <input
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full bg-gray-700 text-white rounded-lg px-4 py-2
                         focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          {error && <p className="text-red-400 text-sm">{error}</p>}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50
                       text-white py-2 rounded-lg font-medium transition-colors"
          >
            {loading ? 'Signing in...' : 'Sign in'}
          </button>
        </form>

        <p className="text-gray-500 text-sm text-center mt-4">
          No account?{' '}
          <Link href="/register" className="text-indigo-400 hover:underline">
            Register
          </Link>
        </p>
      </div>
    </div>
  );
}