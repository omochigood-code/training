import type { PageServerLoad } from './$types';
import { searchUsers } from '$lib/api';

export const load: PageServerLoad = async ({ fetch }) => {
  const users = await searchUsers({}, fetch);
  return { users };
};
