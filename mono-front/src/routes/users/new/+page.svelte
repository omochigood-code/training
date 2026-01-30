<script lang="ts">
  import { goto } from '$app/navigation';
  import { createUser, type CreateUser } from '$lib/api';

  let form: CreateUser = { name: '' };
  let saving = false;

  async function save() {
    if (!form.name) {
      alert('名前は必須です');
      return;
    }

    saving = true;
    try {
      await createUser({ name: form.name }, fetch);
      goto('/users');
    } catch (e) {
      console.error(e);
      alert('登録に失敗しました');
    } finally {
      saving = false;
    }
  }
</script>

<h1>新規ユーザー登録</h1>

<div>
  <label>
    名前：
    <input bind:value={form.name} placeholder="名前" />
  </label>
</div>

<div style="margin-top: 12px;">
  <button on:click={save} disabled={saving}>
    {saving ? '登録中...' : '登録'}
  </button>
  <button on:click={() => goto('/users')} style="margin-left: 8px;" disabled={saving}>
    戻る
  </button>
</div>
