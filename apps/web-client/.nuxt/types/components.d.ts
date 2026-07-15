
import type { DefineComponent, SlotsType } from 'vue'
type IslandComponent<T> = DefineComponent<{}, {refresh: () => Promise<void>}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, SlotsType<{ fallback: { error: unknown } }>> & T

type HydrationStrategies = {
  hydrateOnVisible?: IntersectionObserverInit | true
  hydrateOnIdle?: number | true
  hydrateOnInteraction?: keyof HTMLElementEventMap | Array<keyof HTMLElementEventMap> | true
  hydrateOnMediaQuery?: string
  hydrateAfter?: number
  hydrateWhen?: boolean
  hydrateNever?: true
}
type LazyComponent<T> = DefineComponent<HydrationStrategies, {}, {}, {}, {}, {}, {}, { hydrated: () => void }> & T

interface _GlobalComponents {
  Acquaint: typeof import("../../components/Acquaint.vue")['default']
  EditorTiptapEditor: typeof import("../../components/editor/TiptapEditor.vue")['default']
  MascotCompanionWidget: typeof import("../../components/mascot/CompanionWidget.vue")['default']
  UiButton: typeof import("../../components/ui/button/Button.vue")['default']
  UiDialog: typeof import("../../components/ui/dialog/Dialog.vue")['default']
  UiDialogClose: typeof import("../../components/ui/dialog/DialogClose.vue")['default']
  UiDialogContent: typeof import("../../components/ui/dialog/DialogContent.vue")['default']
  UiDialogDescription: typeof import("../../components/ui/dialog/DialogDescription.vue")['default']
  UiDialogFooter: typeof import("../../components/ui/dialog/DialogFooter.vue")['default']
  UiDialogHeader: typeof import("../../components/ui/dialog/DialogHeader.vue")['default']
  UiDialogScrollContent: typeof import("../../components/ui/dialog/DialogScrollContent.vue")['default']
  UiDialogTitle: typeof import("../../components/ui/dialog/DialogTitle.vue")['default']
  UiDialogTrigger: typeof import("../../components/ui/dialog/DialogTrigger.vue")['default']
  UiDropdownMenu: typeof import("../../components/ui/dropdown-menu/DropdownMenu.vue")['default']
  UiDropdownMenuCheckboxItem: typeof import("../../components/ui/dropdown-menu/DropdownMenuCheckboxItem.vue")['default']
  UiDropdownMenuContent: typeof import("../../components/ui/dropdown-menu/DropdownMenuContent.vue")['default']
  UiDropdownMenuGroup: typeof import("../../components/ui/dropdown-menu/DropdownMenuGroup.vue")['default']
  UiDropdownMenuItem: typeof import("../../components/ui/dropdown-menu/DropdownMenuItem.vue")['default']
  UiDropdownMenuLabel: typeof import("../../components/ui/dropdown-menu/DropdownMenuLabel.vue")['default']
  UiDropdownMenuRadioGroup: typeof import("../../components/ui/dropdown-menu/DropdownMenuRadioGroup.vue")['default']
  UiDropdownMenuRadioItem: typeof import("../../components/ui/dropdown-menu/DropdownMenuRadioItem.vue")['default']
  UiDropdownMenuSeparator: typeof import("../../components/ui/dropdown-menu/DropdownMenuSeparator.vue")['default']
  UiDropdownMenuShortcut: typeof import("../../components/ui/dropdown-menu/DropdownMenuShortcut.vue")['default']
  UiDropdownMenuSub: typeof import("../../components/ui/dropdown-menu/DropdownMenuSub.vue")['default']
  UiDropdownMenuSubContent: typeof import("../../components/ui/dropdown-menu/DropdownMenuSubContent.vue")['default']
  UiDropdownMenuSubTrigger: typeof import("../../components/ui/dropdown-menu/DropdownMenuSubTrigger.vue")['default']
  UiDropdownMenuTrigger: typeof import("../../components/ui/dropdown-menu/DropdownMenuTrigger.vue")['default']
  UiInput: typeof import("../../components/ui/input/Input.vue")['default']
  NuxtWelcome: typeof import("../../node_modules/nuxt/dist/app/components/welcome.vue")['default']
  NuxtLayout: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-layout")['default']
  NuxtErrorBoundary: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-error-boundary.vue")['default']
  ClientOnly: typeof import("../../node_modules/nuxt/dist/app/components/client-only")['default']
  DevOnly: typeof import("../../node_modules/nuxt/dist/app/components/dev-only")['default']
  ServerPlaceholder: typeof import("../../node_modules/nuxt/dist/app/components/server-placeholder")['default']
  NuxtLink: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-link")['default']
  NuxtLoadingIndicator: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-loading-indicator")['default']
  NuxtTime: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-time.vue")['default']
  NuxtRouteAnnouncer: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-route-announcer")['default']
  NuxtImg: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-stubs")['NuxtImg']
  NuxtPicture: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-stubs")['NuxtPicture']
  NuxtPage: typeof import("../../node_modules/nuxt/dist/pages/runtime/page")['default']
  NoScript: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['NoScript']
  Link: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Link']
  Base: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Base']
  Title: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Title']
  Meta: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Meta']
  Style: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Style']
  Head: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Head']
  Html: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Html']
  Body: typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Body']
  NuxtIsland: typeof import("../../node_modules/nuxt/dist/app/components/nuxt-island")['default']
  LazyAcquaint: LazyComponent<typeof import("../../components/Acquaint.vue")['default']>
  LazyEditorTiptapEditor: LazyComponent<typeof import("../../components/editor/TiptapEditor.vue")['default']>
  LazyMascotCompanionWidget: LazyComponent<typeof import("../../components/mascot/CompanionWidget.vue")['default']>
  LazyUiButton: LazyComponent<typeof import("../../components/ui/button/Button.vue")['default']>
  LazyUiDialog: LazyComponent<typeof import("../../components/ui/dialog/Dialog.vue")['default']>
  LazyUiDialogClose: LazyComponent<typeof import("../../components/ui/dialog/DialogClose.vue")['default']>
  LazyUiDialogContent: LazyComponent<typeof import("../../components/ui/dialog/DialogContent.vue")['default']>
  LazyUiDialogDescription: LazyComponent<typeof import("../../components/ui/dialog/DialogDescription.vue")['default']>
  LazyUiDialogFooter: LazyComponent<typeof import("../../components/ui/dialog/DialogFooter.vue")['default']>
  LazyUiDialogHeader: LazyComponent<typeof import("../../components/ui/dialog/DialogHeader.vue")['default']>
  LazyUiDialogScrollContent: LazyComponent<typeof import("../../components/ui/dialog/DialogScrollContent.vue")['default']>
  LazyUiDialogTitle: LazyComponent<typeof import("../../components/ui/dialog/DialogTitle.vue")['default']>
  LazyUiDialogTrigger: LazyComponent<typeof import("../../components/ui/dialog/DialogTrigger.vue")['default']>
  LazyUiDropdownMenu: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenu.vue")['default']>
  LazyUiDropdownMenuCheckboxItem: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuCheckboxItem.vue")['default']>
  LazyUiDropdownMenuContent: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuContent.vue")['default']>
  LazyUiDropdownMenuGroup: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuGroup.vue")['default']>
  LazyUiDropdownMenuItem: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuItem.vue")['default']>
  LazyUiDropdownMenuLabel: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuLabel.vue")['default']>
  LazyUiDropdownMenuRadioGroup: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuRadioGroup.vue")['default']>
  LazyUiDropdownMenuRadioItem: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuRadioItem.vue")['default']>
  LazyUiDropdownMenuSeparator: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuSeparator.vue")['default']>
  LazyUiDropdownMenuShortcut: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuShortcut.vue")['default']>
  LazyUiDropdownMenuSub: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuSub.vue")['default']>
  LazyUiDropdownMenuSubContent: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuSubContent.vue")['default']>
  LazyUiDropdownMenuSubTrigger: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuSubTrigger.vue")['default']>
  LazyUiDropdownMenuTrigger: LazyComponent<typeof import("../../components/ui/dropdown-menu/DropdownMenuTrigger.vue")['default']>
  LazyUiInput: LazyComponent<typeof import("../../components/ui/input/Input.vue")['default']>
  LazyNuxtWelcome: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/welcome.vue")['default']>
  LazyNuxtLayout: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-layout")['default']>
  LazyNuxtErrorBoundary: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-error-boundary.vue")['default']>
  LazyClientOnly: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/client-only")['default']>
  LazyDevOnly: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/dev-only")['default']>
  LazyServerPlaceholder: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/server-placeholder")['default']>
  LazyNuxtLink: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-link")['default']>
  LazyNuxtLoadingIndicator: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-loading-indicator")['default']>
  LazyNuxtTime: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-time.vue")['default']>
  LazyNuxtRouteAnnouncer: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-route-announcer")['default']>
  LazyNuxtImg: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-stubs")['NuxtImg']>
  LazyNuxtPicture: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-stubs")['NuxtPicture']>
  LazyNuxtPage: LazyComponent<typeof import("../../node_modules/nuxt/dist/pages/runtime/page")['default']>
  LazyNoScript: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['NoScript']>
  LazyLink: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Link']>
  LazyBase: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Base']>
  LazyTitle: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Title']>
  LazyMeta: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Meta']>
  LazyStyle: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Style']>
  LazyHead: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Head']>
  LazyHtml: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Html']>
  LazyBody: LazyComponent<typeof import("../../node_modules/nuxt/dist/head/runtime/components")['Body']>
  LazyNuxtIsland: LazyComponent<typeof import("../../node_modules/nuxt/dist/app/components/nuxt-island")['default']>
}

declare module 'vue' {
  export interface GlobalComponents extends _GlobalComponents { }
}

export {}
