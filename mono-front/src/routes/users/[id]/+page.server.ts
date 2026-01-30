import type { PageServerLoad } from './$types';
import { getUser } from '$lib/api';
import { error } from '@sveltejs/kit';

export const load: PageServerLoad = async ({ params, fetch }) => {
  const id = Number(params.id);

  if (!Number.isInteger(id)) {
    throw error(400, '不正なユーザーIDです');
  }

  try {
    const user = await getUser(id, fetch);
    return { user };
  } catch {
    throw error(404, 'ユーザーが見つかりません');
  }
};
