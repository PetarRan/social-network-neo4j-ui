import { nothing } from 'lit-html';
import { PropertyValues, html, LitElement, customElement } from 'lit-element';
import * as L from 'leaflet';




const openStreetMapLayer = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
const openStreetMapAttribution = `&copy; <a href='https://www.openstreetmap.org/copyright'>OpenStreetMap</a> contributors`;

@customElement('leaflet-map')
export class LeafletMap extends LitElement {
  private map!: L.Map;

  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`${nothing}`;
  }

  firstUpdated(_changedProperties: PropertyValues) {
    super.firstUpdated(_changedProperties);

    this.map = L.map(this);
    let tileLayer = L.tileLayer(openStreetMapLayer, { attribution: openStreetMapAttribution, maxZoom: 13 });
    tileLayer.addTo(this.map);
  }

  async setView(latitude: number, longitude: number, zoomLevel: number) {
    await this.updateComplete; // Make sure map has been initialized
    this.map.setView([latitude, longitude], zoomLevel);
  }
}
