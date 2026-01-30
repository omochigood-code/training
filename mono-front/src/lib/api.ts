export const BASE_URL = 'http://localhost:8080';

/* =====================
 * 型定義
 * ===================== */

export type User = {
  id: number;
  name: string;
};

export type CreateUser = {
  name: string;
};

export type UpdateUser = {
  name: string;
};

/* =====================
 * API
 * ===================== */

/**
 * ユーザー一覧 / 検索
 * GET /users?id=&name=
 */
export async function searchUsers(
  params: { id?: string; name?: string },
  fetchFn: typeof fetch = fetch
): Promise<User[]> {
  const query = new URLSearchParams();

  if (params.id) query.append('id', params.id);
  if (params.name) query.append('name', params.name);

  const res = await fetchFn(`${BASE_URL}/users?${query.toString()}`);
  if (!res.ok) throw new Error('検索失敗');

  return res.json();
}

/**
 * ユーザー取得
 * GET /users/{id}
 */
export async function getUser(
  id: number,
  fetchFn: typeof fetch = fetch
): Promise<User> {
  const res = await fetchFn(`${BASE_URL}/users/${id}`);
  if (!res.ok) throw new Error('取得失敗');

  return res.json();
}

/**
 * ユーザー新規作成
 * POST /users
 */
export async function createUser(
  user: CreateUser,
  fetchFn: typeof fetch = fetch
): Promise<User> {
  const res = await fetchFn(`${BASE_URL}/users`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(user)
  });

  if (!res.ok) throw new Error('作成失敗');
  return res.json();
}

/**
 * ユーザー更新
 * PUT /users/{id}
 */
export async function updateUser(
  id: number,
  user: UpdateUser,
  fetchFn: typeof fetch = fetch
): Promise<User> {
  const res = await fetchFn(`${BASE_URL}/users/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(user)
  });

  if (!res.ok) throw new Error('更新失敗');
  return res.json();
}

/**
 * ユーザー削除
 * DELETE /users/{id}
 */
export async function deleteUser(
  id: number,
  fetchFn: typeof fetch = fetch
): Promise<void> {
  const res = await fetchFn(`${BASE_URL}/users/${id}`, {
    method: 'DELETE'
  });

  if (!res.ok) throw new Error('削除失敗');
}
