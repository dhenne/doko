<script>
	import '../app.pcss';
	import { page } from '$app/stores';
</script>

{#if $page.url.pathname !== '/'}
	<header class="sticky top-0 z-40 w-full backdrop-blur-md bg-[#0f0f1a]/85 border-b border-white/5 px-4 py-3 flex items-center justify-between shadow-lg shadow-black/20">
		<a href="/" class="flex items-center gap-2 text-xl font-extrabold tracking-wider bg-gradient-to-r from-[#FE795D] to-[#EB4F27] bg-clip-text text-transparent uppercase">
			DOKO
		</a>
		<div class="flex items-center gap-4">
			<a href="/" class="text-xs uppercase tracking-wider font-bold text-slate-400 hover:text-[#FE795D] transition-all duration-200">
				Game
			</a>
			<a href="/game/create" class="text-xs uppercase tracking-wider font-bold text-slate-400 hover:text-[#FE795D] transition-all duration-200">
				New Game
			</a>
		</div>
	</header>
{/if}

<slot />
