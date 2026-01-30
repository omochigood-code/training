<script lang="ts">
  import { goto } from '$app/navigation';
  import { onMount } from 'svelte';
  import { searchUsers, type User } from '$lib/api';

  /* =====================
   * 入力中の値（即時反映しない）
   * ===================== */
  let inputUserId = '';
  let inputUserName = '';

  /* =====================
   * 検索確定条件
   * ===================== */
  let condition = {
    userId: '',
    userName: ''
  };

  let users: User[] = [];

  /* =====================
   * ソート・ページング
   * ===================== */
  let sortKey: keyof User = 'id';
  let asc = true;

  let page = 1;
  const pageSize = 5;

  /* =====================
   * 初期取得（一覧）
   * ===================== */
  onMount(async () => {
    users = await searchUsers({}, fetch);
  });

  /* =====================
   * 検索確定
   * ===================== */
  function search() {
    condition = {
      userId: inputUserId,
      userName: inputUserName
    };
    page = 1;
  }

  function sort(key: keyof User) {
    asc = sortKey === key ? !asc : true;
    sortKey = key;
  }

  function goDetail(id: number) {
    goto(`/users/${id}`);
  }

  function goNewUser() {
    goto('/users/new');
  }

  /* =====================
   * フィルタ → ソート → ページング
   * ===================== */
  $: filteredUsers = users.filter(u => {
    if (condition.userId && String(u.id) !== condition.userId) return false;
    if (condition.userName && !u.name.includes(condition.userName)) return false;
    return true;
  });

  $: sortedUsers = [...filteredUsers].sort((a, b) => {
    const v1 = a[sortKey];
    const v2 = b[sortKey];
    return asc ? (v1 > v2 ? 1 : -1) : (v1 < v2 ? 1 : -1);
  });

  $: pagedUsers = sortedUsers.slice(
    (page - 1) * pageSize,
    page * pageSize
  );

  $: totalPages = Math.ceil(sortedUsers.length / pageSize);
</script>

<h1>ユーザー検索</h1>

<div>
  <input bind:value={inputUserId} placeholder="ユーザーID" />
  <input bind:value={inputUserName} placeholder="ユーザー名" />
  <button on:click={search}>検索</button>
  <button on:click={goNewUser}>新規登録</button>
</div>

<table border="1">
  <thead>
    <tr>
      <th on:click={() => sort('id')}>ID</th>
      <th on:click={() => sort('name')}>名前</th>
      <th>詳細</th>
    </tr>
  </thead>
  <tbody>
    {#each pagedUsers as user}
      <tr>
        <td>{user.id}</td>
        <td>{user.name}</td>
        <td>
          <button on:click={() => goDetail(user.id)}>詳細</button>
        </td>
      </tr>
    {/each}
  </tbody>
</table>

<button disabled={page === 1} on:click={() => page--}>前へ</button>
{page} / {totalPages}
<button disabled={page === totalPages} on:click={() => page++}>次へ</button>
