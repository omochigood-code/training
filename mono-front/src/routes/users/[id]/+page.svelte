<script lang="ts">
  import { goto } from '$app/navigation';
  import { updateUser, deleteUser, type User } from '$lib/api';

  export let data: {
    user: User;
  };

  let name = data.user.name;

  async function save() {
    if (!name) {
      alert('名前は必須です');
      return;
    }

    await updateUser(data.user.id, { name }, fetch);
    goto('/users');
  }

  async function remove() {
    if (!confirm('削除しますか？')) return;
    await deleteUser(data.user.id, fetch);
    goto('/users');
  }
</script>

<h1>ユーザー詳細</h1>

<input bind:value={name} />

<button on:click={save}>更新</button>
<button on:click={remove}>削除</button>
<button on:click={() => goto('/users')}>戻る</button>
